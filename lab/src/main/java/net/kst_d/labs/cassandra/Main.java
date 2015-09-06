package net.kst_d.labs.cassandra;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import rx.Observable;

public class Main {

    public static final Pattern ROW_SPLITTER = Pattern.compile("\t");

    public static final List<Two<String, Function<String, Object>>> ROWS = Arrays.asList(
		    Two.of(Constants.ID, s -> s),
		    Two.of(Constants.TERMINAL_ID, s -> s),
		    Two.of(Constants.SERVICE_ID, s -> s),
		    Two.of(Constants.DATA, Main::prepareDate2),
		    Two.of(Constants.PACC, s -> s),
		    Two.of(Constants.AMOUNT, Main::prepareInt), //7
		    Two.of(Constants.SENT_DATE, Main::prepareDate2),
		    Two.of(Constants.DESCRIBE, s -> s),
		    Two.of(Constants.UNICUMCODE, s -> s),
		    Two.of(Constants.STATE, s -> s),
		    Two.of(Constants.ERROR_CODE, s -> s),
		    Two.of(Constants.ERROR_TEXT, s -> s),
		    Two.of(Constants.PS_PAY_NUM, Main::prepareLong),//14
		    Two.of(Constants.OUT_SERV_ID, s -> s),
		    Two.of(Constants.ACC_ORG_ID, s -> s),
		    Two.of(Constants.ORG_STATE, s -> s),
		    Two.of(Constants.ORDER_DATE, Main::prepareDate2),
		    Two.of(Constants.SEND_DATE, Main::prepareDate2),
		    Two.of(Constants.PROV_PAY_ID, s -> s),
		    Two.of(Constants.ATTR1, s -> s),
		    Two.of(Constants.AGENT_FEE_AMOUNT, Main::prepareLong),//22
		    Two.of(Constants.BANK_FEE_AMOUNT, Main::prepareLong),
		    Two.of(Constants.PAY_TYPE, Main::prepareInt),
		    Two.of(Constants.CHECK_NUM, s -> s),
		    Two.of(Constants.RCPT_ORG_ID, Main::prepareLong),//26
		    Two.of(Constants.AGENT_DATE, Main::prepareDate2),
		    Two.of(Constants.PROV_PAY_DATE, Main::prepareDate2),
		    Two.of(Constants.SERV_ROW_ID, s -> s),
		    Two.of(Constants.ATTR2, s -> s),
		    Two.of(Constants.AGENT_FEE_TYPE, s -> s),
		    Two.of(Constants.BANK_FEE_TYPE, s -> s),
		    Two.of(Constants.FLAGS, s -> s),
		    Two.of(Constants.AGENT_FEE2_TYPE, s -> s),
		    Two.of(Constants.AGENT_FEE2_AMOUNT, s -> s),
		    Two.of(Constants.BANK_FEE2_TYPE, s -> s),
		    Two.of(Constants.BANK_FEE2_AMOUNT, s -> s),
		    Two.of(Constants.PARENT_PAY_ID, s -> s),
		    Two.of(Constants.OUT_TERM_ID, s -> s)
    );

    public static void main(String[] args) {
	try (final Cluster cluster = Cluster.builder()
			.addContactPoint("192.168.56.30")
			.addContactPoint("192.168.56.31")
			.addContactPoint("192.168.56.32")
			.addContactPoint("192.168.56.33")
			.build();
	     final Session session = cluster.connect("lab0")
	) {
	    final String file = "some-data.tsv";
	    final Observable<Two<Long, String>> fileObservable = fileLines(file, Charset.forName("windows-1251"));

	    final Observable<CompletableFuture<Two<Long, List<Two<String, Object>>>>> parsed = fileObservable.map(
			    s -> CompletableFuture.supplyAsync(() -> parseRow(s._1, s._2)));

	    final Observable<CompletableFuture<ResultSet>> futures = parsed.map(f -> f.thenApply(row -> cassandraWriter(row._1, row._2, session)));

	    final Observable<CompletableFuture<List<ResultSet>>> total = futures.reduce(
			    CompletableFuture.completedFuture(new ArrayList<>()), (before, future) -> before.thenCombine(future, (l, r) -> {
		l.add(r);
		return l;
	    }));

	    long beforeTime = System.currentTimeMillis();
	    final CompletableFuture<List<ResultSet>> last = total.toBlocking().last();
	    final List<ResultSet> results = last.get();
	    Long afterTime = System.currentTimeMillis();
	    System.out.println("duration " + (afterTime - beforeTime) + " result len " + results.size());
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private static Two<Long, List<Two<String, Object>>> parseRow(long num, String line) {
	try {
	    final String[] split = ROW_SPLITTER.split(line, -1);
	    Two<String, Object> arr[] = new Two[split.length + 2];
	    arr[0] = Two.of(Constants._UUID, UUID.randomUUID());
	    arr[1] = Two.of(Constants._SYSNAME, "bpc");
	    for (int i = 0; i < split.length && i < ROWS.size(); i++) {
		String s = split[i];
		try {
		    final Two<String, Function<String, Object>> two = ROWS.get(i);
		    arr[i + 2] = Two.of(two._1, two._2.apply(s));
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	    return Two.of(num, Arrays.asList(arr));
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return Two.of(num, Collections.emptyList());
    }

    private static ResultSet cassandraWriter(Long num, List<Two<String, Object>> data, Session session) {
	try {
	    String fields = data.stream().map(Two::get_1).collect(Collectors.joining(","));
	    String holders = data.stream().map(t -> "?").collect(Collectors.joining(","));
	    final Object[] bind = data.stream().map(Two::get_2).toArray(Object[]::new);

	    final PreparedStatement prepare = session.prepare(
			    "insert into bpc_payment_logs (" + fields + ") values (" + holders + ")");

	    final BoundStatement boundStatement = prepare.bind(bind);

	    return session.execute(boundStatement);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    static Integer prepareInt(String in) {
	return StringUtils.isBlank(in) ? null : Integer.valueOf(in);
    }

    static Long prepareLong(String in) {
	return StringUtils.isBlank(in) ? null : Long.valueOf(in);
    }

    static Date prepareDate2(String in) {
	if (StringUtils.isBlank(in)) {
	    return null;
	}
	final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss");
	return Date.from(LocalDateTime.parse(in, formatter).atZone(ZoneId.systemDefault()).toInstant());
    }

    private static Observable<Two<Long, String>> fileLines(final String name, final Charset charset) {
	final Path path = Paths.get(name);
	return Observable.<Two<Long, String>>create(subscriber -> new Thread(() -> {
	    try {
		final BufferedReader reader = Files.newBufferedReader(path, charset);
		String buff;
		long i = 0;
		while ((buff = reader.readLine()) != null) {
		    if (subscriber.isUnsubscribed()) {
			return;
		    }
		    subscriber.onNext(Two.of(i++, buff));
		}
		if (!subscriber.isUnsubscribed()) {
		    subscriber.onCompleted();
		}
	    } catch (IOException e) {
		subscriber.onError(e);
	    }
	}, "file-reading-thread").start());
    }
}

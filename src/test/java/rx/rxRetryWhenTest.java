package rx;

import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by dshue1 on 5/3/16.
 */
public class rxRetryWhenTest {
	@Test
	public void testRetryWhen() {
		Observable.create(s -> {
			System.out.println("2) subscribing");
			s.onError(new RuntimeException("2) always fails"));
		}).retryWhen(attempts -> {
			return attempts.zipWith(Observable.range(1, 3), (n, i) -> i).flatMap(i -> {
				System.out.println("2) delay retry by " + i + " second(s)");
				return Observable.timer(i, TimeUnit.SECONDS);
			}).concatWith(Observable.error(new RuntimeException("Failed after 3 retries")));
		}).toBlocking().forEach(System.out::println);
	}
}

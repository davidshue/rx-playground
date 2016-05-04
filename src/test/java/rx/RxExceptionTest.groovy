package rx

import org.junit.Test

import java.util.concurrent.TimeUnit
/**
 * Created by dshue1 on 5/3/16.
 */
class RxExceptionTest {
	@Test
	void testRetry() {
		Observable.create{
			println '1) subscribing'
			it.onError(new RuntimeException('1) always fails'))
		}
		.retry(3)
		.subscribe(
			{println it},
			{println '1) Error: ' + it}
		)
	}

	@Test
	void testRetryWhen() {
		Observable.create{
			println '2) subscribing'
			it.onError(new RuntimeException('2) always fails'))
		}
		.retryWhen{
			it.zipWith(Observable.range(1,3), {n, i -> i})
			.flatMap{
				println "2) delay retry by $it seconds"
				Observable.timer(it, TimeUnit.SECONDS)
			}
			.concatWith(
				Observable.error(new RuntimeException('Failed after 3 retries'))
			)
			.toBlocking()
			.forEach{println it}
		}
	}
}

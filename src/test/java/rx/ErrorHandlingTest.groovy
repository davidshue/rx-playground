package rx

import org.junit.Test

/**
 * Created by dshue1 on 5/3/16.
 */
class ErrorHandlingTest {
	@Test
	void testOnError() {
		Observable.create{throw new RuntimeException('failed')}
		.subscribe(
			{println it},
			{println "1) Error: $it"}
		)
	}

	@Test
	void testOnError2() {
		Observable.just('hello')
		.map {throw new RuntimeException('failed')}
		.subscribe(
			{println it},
			{println "2) Error: $it"}
		)
	}

	@Test
	void testOnError3() {
		Observable.just(true)
		.flatMap{
			it ? Observable.error(new RuntimeException('failed')) : Observable.just('data', 'here')
		}
		.subscribe(
			{println it},
			{println "3) Error: $it"}
		)
	}

	@Test
	void testOnError4() {
		Observable.error(new RuntimeException('failed'))
		.onErrorResumeNext(Observable.just('4) data'))
		.subscribe(
			{println it},
			{println "4) Error: $it"}
		)
	}
}

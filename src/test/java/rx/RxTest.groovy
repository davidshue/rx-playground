package rx

import org.junit.Test
/**
 * Created by dshue1 on 4/27/16.
 */
class RxTest {
	private def names = ['joe', 'andy', 'ethan']

	@Test
	void testObservable() {
		Observable.from(names).subscribe{
			println "Hello ${it}!"
		}
	}

	@Test
	void testReduce() {
		Observable.from(1..100)
		.reduce(0){sum, num ->
			sum += num
		}
		//.observeOn(Schedulers.computation())
		.subscribe{println it}
	}

	@Test
	void testFlatMap() {
		def persons = [new Person(gender: Gender.MALE, name:'John', age: 50), new Person(gender: Gender.FEMALE, name:'debbie', age: 20),
			new Person(gender: Gender.MALE, name: 'billy', age: 30), new Person(gender: Gender.MALE, name: 'alex', age: 40)
		]
		Observable.from(persons)
		.groupBy{Person it -> it.gender}
		.flatMap{
			it.reduce([:]) {map, el ->
				map.get(it.key, []) << el
				map
			}
		}
		.subscribe{println it}

		Observable.from(1..3)
			.flatMap{n -> Observable.from([n*2, n* 3])}
			.subscribe(
				{println 'next ' + it},
				{println 'error ' + it},
				{println 'complete '}
			)
	}

	@Test
	void testDefer() {
		Observable.defer{Observable.from(1..100)}
			.subscribe(
			{println 'next ' + it},
			{println 'error ' + it},
			{println 'complete '}
		)
	}

	@Test
	void testRange() {
		Observable.range(10, 10)
			.buffer(3)
			.subscribe(
			{println 'next ' + it},
			{println 'error ' + it},
			{println 'complete '}
		)
	}

	@Test
	void testEmptyErrorNever() {
		println '*** empty() ***'
		Observable.empty().subscribe(
			{ println "empty: $it" },                      // onNext
			{ println "empty: error - $it.message" }, // onError
			{ println "empty: Sequence complete" }           // onCompleted
		)

		println "*** error() ***"
		Observable.error(new Throwable("badness")).subscribe(
			{ println "error: $it" },                      // onNext
			{ println "error: error - $it.message" }, // onError
			{ println "error: Sequence complete" }           // onCompleted
		);

		println "*** never() ***"
		Observable.never().subscribe(
			{ println "never: $it" },                      // onNext
			{ println "never: error - $it.message" }, // onError
			{ println "never: Sequence complete" }           // onCompleted
		);
		println "*** END ***"
	}
}

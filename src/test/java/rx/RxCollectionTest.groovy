package rx

import org.junit.Test
import rx.schedulers.Schedulers
/**
 * Created by dshue1 on 5/3/16.
 */
class RxCollectionTest {
	/**
	 * This should take only 10 seconds
	 */
	@Test
	void testMergeAsync() {
		long start = System.currentTimeMillis()
		Observable.merge(getDataAsync(1), getDataAsync(2))
			.toBlocking()
			.toIterable()
			.each {println it}

		long end = System.currentTimeMillis()

		println 'Took ' + (end-start) + ' ms'
	}

	/**
	 * This should take 20 seconds
	 */
	@Test
	void testMergeSync() {
		long start = System.currentTimeMillis()
		Observable.merge(getDataSync(1), getDataSync(2))
			.toBlocking()
			.toIterable()
			.each {println it}

		long end = System.currentTimeMillis()

		println 'Took ' + (end-start) + ' ms'
	}

	/**
	 * Convert Sync calls to Async calls, just magic!!!
	 * Should take only 10 seconds here
	 */
	@Test
	void testMergeSync2Async() {
		long start = System.currentTimeMillis()
		Observable.merge(getDataSync(1).subscribeOn(Schedulers.io()), getDataSync(2).subscribeOn(Schedulers.io()))
			.toBlocking()
			.toIterable()
			.each {println it}

		long end = System.currentTimeMillis()

		println 'Took ' + (end-start) + ' ms'
	}

	@Test
	void testFlatMapExampleAsync() {
		long start = System.currentTimeMillis()
		Observable
			.range(0, 5)
			.flatMap{i -> getDataAsync(i)}
			.toBlocking()
			.forEach{println it}

		long end = System.currentTimeMillis()

		println 'Took ' + (end-start) + ' ms'
	}



	Observable<Integer> getDataAsync(int i) {
		return getDataSync(i).subscribeOn(Schedulers.io());
	}

	Observable<Integer> getDataSync(int i) {
		Observable.create{
			sleep 10000
			it.onNext(i)
			it.onCompleted()
		}
	}
}

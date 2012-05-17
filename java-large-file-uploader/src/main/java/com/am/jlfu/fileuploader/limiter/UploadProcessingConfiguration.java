package com.am.jlfu.fileuploader.limiter;


public class UploadProcessingConfiguration {

	/**
	 * Specifies the amount of bytes that have been written
	 * */
	private long bytesWritten;
	private Object bytesWrittenLock = new Object();

	/**
	 * Specifies the amount of bytes that can be uploaded for an iteration of the refill process
	 * of {@link RateLimiter}
	 * */
	private long downloadAllowanceForIteration;
	private Object downloadAllowanceForIterationLock = new Object();

	/**
	 * The desired upload rate. <br>
	 * Can be null (the maxmimum rate is applied).
	 */
	volatile Long rateInKiloBytes;

	/**
	 * The statistics.
	 * 
	 * @return
	 */
	volatile long instantRateInBytes;



	public Long getRateInKiloBytes() {
		return rateInKiloBytes;
	}


	public long getDownloadAllowanceForIteration() {
		synchronized (downloadAllowanceForIterationLock) {
			return downloadAllowanceForIteration;
		}
	}


	void setDownloadAllowanceForIteration(long downloadAllowanceForIteration) {
		synchronized (downloadAllowanceForIterationLock) {
			this.downloadAllowanceForIteration = downloadAllowanceForIteration;
		}
	}


	public long getAndResetBytesWritten() {
		synchronized (bytesWrittenLock) {
			final long temp = bytesWritten;
			bytesWritten = 0;
			return temp;
		}
	}


	/**
	 * Specifies the bytes that have been read from the files.
	 * 
	 * @param bytesConsumed
	 */
	public void bytesConsumedFromAllowance(long bytesConsumed) {
		synchronized (bytesWrittenLock) {
			synchronized (downloadAllowanceForIterationLock) {
				bytesWritten += bytesConsumed;
				downloadAllowanceForIteration -= bytesConsumed;
			}
		}
	}


	void setInstantRateInBytes(long instantRateInBytes) {
		this.instantRateInBytes = instantRateInBytes;
	}


	long getInstantRateInBytes() {
		return instantRateInBytes;
	}


}
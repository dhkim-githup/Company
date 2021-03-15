public class ZeroPadding implements CryptoPadding {
	
	/** 패딩 규칙 이름. */
	private String name = "ZeroPadding";
	
	/** PADDIN g_ value. */
	private final byte PADDING_VALUE = 0x00;
	
	/**
	 * 요청한 Block Size를 맞추기 위해 Padding을 추가한다.
	 * 
	 * @param source
	 *            byte[] 패딩을 추가할 bytes
	 * @param blockSize
	 *            int block size
	 * @return byte[] 패딩이 추가 된 결과 bytes
	 */
	public byte[] addPadding(byte[] source, int blockSize) {
		int paddingCnt = source.length % blockSize;
		byte[] paddingResult = null;
		
		if(paddingCnt != 0) {
			paddingResult = new byte[source.length + (blockSize - paddingCnt)];
			
			System.arraycopy(source, 0, paddingResult, 0, source.length);
			
			// 패딩해야 할 갯수 까지 0x00 값을 추가한다.
			int addPaddingCnt = blockSize - paddingCnt;
			for(int i=0;i<addPaddingCnt;i++) {
				paddingResult[source.length + i] = PADDING_VALUE;
			}
		} else {
			paddingResult = source;
		}

		return paddingResult;
	}

	/**
	 * 요청한 Block Size를 맞추기 위해 추가 된 Padding을 제거한다.
	 * 
	 * @param source
	 *            byte[] 패딩을 제거할 bytes
	 * @param blockSize
	 *            int block size
	 * @return byte[] 패딩이 제거 된 결과 bytes
	 */
	public byte[] removePadding(byte[] source, int blockSize) {
		byte[] paddingResult = null;
		boolean isPadding = false;
		
		// 패딩 된 count를 찾는다.
		int lastValue = 0;

		for(int i=1;i<source.length;i++) {
			if(source[source.length - i] != PADDING_VALUE) {
				break;
			} else {
				lastValue++;
			}
		}
				
		if(lastValue > 0) {
			paddingResult = new byte[source.length - lastValue];
			System.arraycopy(source, 0, paddingResult, 0, paddingResult.length);
		} else {
			paddingResult = source;
		}		
		
		return paddingResult;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
}
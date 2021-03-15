public class ZeroPadding implements CryptoPadding {
	
	/** �е� ��Ģ �̸�. */
	private String name = "ZeroPadding";
	
	/** PADDIN g_ value. */
	private final byte PADDING_VALUE = 0x00;
	
	/**
	 * ��û�� Block Size�� ���߱� ���� Padding�� �߰��Ѵ�.
	 * 
	 * @param source
	 *            byte[] �е��� �߰��� bytes
	 * @param blockSize
	 *            int block size
	 * @return byte[] �е��� �߰� �� ��� bytes
	 */
	public byte[] addPadding(byte[] source, int blockSize) {
		int paddingCnt = source.length % blockSize;
		byte[] paddingResult = null;
		
		if(paddingCnt != 0) {
			paddingResult = new byte[source.length + (blockSize - paddingCnt)];
			
			System.arraycopy(source, 0, paddingResult, 0, source.length);
			
			// �е��ؾ� �� ���� ���� 0x00 ���� �߰��Ѵ�.
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
	 * ��û�� Block Size�� ���߱� ���� �߰� �� Padding�� �����Ѵ�.
	 * 
	 * @param source
	 *            byte[] �е��� ������ bytes
	 * @param blockSize
	 *            int block size
	 * @return byte[] �е��� ���� �� ��� bytes
	 */
	public byte[] removePadding(byte[] source, int blockSize) {
		byte[] paddingResult = null;
		boolean isPadding = false;
		
		// �е� �� count�� ã�´�.
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
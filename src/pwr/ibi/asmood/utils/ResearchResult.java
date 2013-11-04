package pwr.ibi.asmood.utils;

public class ResearchResult {

	private String testType, asn, host;
	private int result;
	private long time;
	
	public ResearchResult(String testType, String asn, String host, int result, long time) {
		super();
		this.testType = testType;
		this.asn = asn;
		this.host = host;
		this.result = result;
		this.time = time;
	}
	
	public ResearchResult(String[] fields){
		testType = fields[0];
		asn = fields[1];
		host = fields[2]; 
		result = Integer.parseInt(fields[3]);
		time = Long.parseLong(fields[4]);
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getAsn() {
		return asn;
	}

	public void setAsn(String asn) {
		this.asn = asn;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "ResearchResult [testType=" + testType + ", asn=" + asn
				+ ", host=" + host + ", result=" + result + ", time=" + time
				+ "]";
	}
}
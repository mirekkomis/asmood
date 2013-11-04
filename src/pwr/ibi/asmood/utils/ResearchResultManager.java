package pwr.ibi.asmood.utils;

import java.util.ArrayList;

public class ResearchResultManager {
	private ResearchResult[] researchResult;
	private ArrayList<String> asnList;
	private ArrayList<ResearchResult> asnPingTests;
	private ArrayList<ResearchResult> asnTracerouteTests;
	
	public ResearchResultManager(ResearchResult[] researchResults) {
		this.researchResult = researchResults;
		createAsnList();
	}
	
	public void createAsnList(){
		asnList = new ArrayList<String>();
		String asn = null;
		for(ResearchResult item: researchResult){
			asn = item.getAsn();
			if(!asnList.contains(asn)){
				asnList.add(asn);
			}
		}
	}

	public void readAsnTests(String asn){
		asnPingTests = new ArrayList<ResearchResult>();
		asnTracerouteTests = new ArrayList<ResearchResult>();
		
		for(ResearchResult item: researchResult)
			if(item.getAsn().equals(asn))
				if(item.getTestType().equals("PING"))
					asnPingTests.add(item);
				else
					asnTracerouteTests.add(item);
	}
	
	public ArrayList<String> getAsnList() {
		return asnList;
	}

	public void setAsnList(ArrayList<String> asnList) {
		this.asnList = asnList;
	}

	public ResearchResult[] getResearchResult() {
		return researchResult;
	}

	public void setResearchResult(ResearchResult[] researchResult) {
		this.researchResult = researchResult;
	}

	public ArrayList<ResearchResult> getAsnPingTests() {
		return asnPingTests;
	}

	public void setAsnPingTests(ArrayList<ResearchResult> asnPingTests) {
		this.asnPingTests = asnPingTests;
	}

	public ArrayList<ResearchResult> getAsnTracerouteTests() {
		return asnTracerouteTests;
	}

	public void setAsnTracerouteTests(ArrayList<ResearchResult> asnTracerouteTests) {
		this.asnTracerouteTests = asnTracerouteTests;
	}
}

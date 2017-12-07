package com.mjkim.concurrency.simplews.message;

import java.util.List;

public class DBQueryResponse extends Response {
	final static RESPONSE_TYPE responseType = RESPONSE_TYPE.DB_SIMPLE_QUERY;
	private List allAddresses;

	@Override
	public RESPONSE_TYPE getResponseType() {
		return responseType;
	}

	public List getAllAddresses() {
		return allAddresses;
	}

	public void setAllAddresses(List allAddresses) {
		this.allAddresses = allAddresses;
	}
}

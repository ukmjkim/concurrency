package com.mjkim.concurrency.simpleserver.common;

import com.mjkim.concurrency.simpleserver.data.WDIDAO;

public class ReportCommand implements Command {
	protected String[] command;

	public ReportCommand(String[] command) {
		this.command = command;
	}

	@Override
	public String execute() {
		WDIDAO dao = WDIDAO.getDAO();
		return dao.report(command[1]);
	}
}

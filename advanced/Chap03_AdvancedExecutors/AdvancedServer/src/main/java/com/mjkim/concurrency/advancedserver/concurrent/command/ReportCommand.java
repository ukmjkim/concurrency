package com.mjkim.concurrency.advancedserver.concurrent.command;

import java.net.Socket;

import com.mjkim.concurrency.advancedserver.wdi.data.WDIDAO;

public class ReportCommand extends Command {
	public ReportCommand(Socket socket, String[] command) {
		super(socket, command);
	}

	@Override
	public String execute() {
		WDIDAO dao = WDIDAO.getDAO();
		return dao.report(command[3]);
	}
}

package org.bumishakti.shell.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.shell.Bootstrap;
import org.springframework.shell.core.CommandResult;
import org.springframework.shell.core.JLineShellComponent;

public class RedmineCommandTests {

	@Test
	public void testConnect() {
		Bootstrap bootstrap = new Bootstrap();
		
		JLineShellComponent shell = bootstrap.getJLineShellComponent();
		
		CommandResult cr = shell.executeCommand("connect");
		assertEquals(false, cr.isSuccess());
		assertEquals("Message = Error", cr.getResult());
	}
}

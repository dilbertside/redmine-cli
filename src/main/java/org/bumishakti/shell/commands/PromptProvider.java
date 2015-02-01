package org.bumishakti.shell.commands;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultPromptProvider;
import org.springframework.stereotype.Component;

/**
 * @author dbs
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PromptProvider extends DefaultPromptProvider {

	@Override
	public String getPrompt() {
		return "red-shell> ";
	}

	
	@Override
	public String getProviderName() {
		return "Releng prompt provider";
	}

}

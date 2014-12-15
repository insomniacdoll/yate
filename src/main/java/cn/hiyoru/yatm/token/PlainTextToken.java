package cn.hiyoru.yatm.token;

import cn.hiyoru.yatm.TemplateContext;

public class PlainTextToken extends AbstractToken {
	public PlainTextToken(String text) {
		setText(text);
	}

	public Object evaluate(TemplateContext context) {
		return getText();
	}

	@Override
	public String emit() {
		return getText();
	}
}

package cn.hiyoru.yatm.token;

import cn.hiyoru.yatm.TemplateContext;


public class EndToken extends AbstractToken {
	public static final String END = "end";

	@Override
	public String getText() {
		if (text == null) {
			text = END;
		}
		return text;
	}

	public Object evaluate(TemplateContext context) {
		return "";
	}
}

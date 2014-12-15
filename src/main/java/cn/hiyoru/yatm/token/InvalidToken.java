package cn.hiyoru.yatm.token;

import cn.hiyoru.yatm.TemplateContext;

public class InvalidToken extends AbstractToken {
	public Object evaluate(TemplateContext context) {
		context.engine.getErrorHandler().error("invalid-expression", this);
		return "";
	}
}

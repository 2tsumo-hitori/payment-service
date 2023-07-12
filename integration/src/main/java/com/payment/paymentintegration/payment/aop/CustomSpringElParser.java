package com.payment.paymentintegration.payment.aop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomSpringElParser {
    public static Object getDynamicValue(String[] parameterNames, Object[] args, String key) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        IntStream.range(0, parameterNames.length).forEach(i -> context.setVariable(parameterNames[i], args[i]));

        Object value = parser.parseExpression(key).getValue(context, Object.class);

        return value;
    }
}

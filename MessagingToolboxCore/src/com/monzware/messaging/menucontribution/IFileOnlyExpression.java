package com.monzware.messaging.menucontribution;

import java.util.List;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

public class IFileOnlyExpression extends Expression {

	@Override
	public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {

		Object defaultVariable = context.getDefaultVariable();

		if (defaultVariable instanceof List<?>) {
			List<?> resources = (List<?>) defaultVariable;
			for (Object object : resources) {
				if (!(object instanceof IFile)) {
					return EvaluationResult.FALSE;
				}
			}

			return EvaluationResult.TRUE;
		}

		return EvaluationResult.FALSE;
	}

}

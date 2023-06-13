package br.com.nagem.filters;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import io.opentracing.Span;
import io.opentracing.noop.NoopSpan;
import io.opentracing.util.GlobalTracer;

@Component
public class TraceLogFilter extends Filter<ILoggingEvent> {

	@Override
	public FilterReply decide(ILoggingEvent event) {
		Span span = GlobalTracer.get().activeSpan();
		if (span != null && span != NoopSpan.INSTANCE) {
			MDC.put("trace_id", "trace_id=" + span.context().toTraceId());
			return FilterReply.ACCEPT;
		} else {
			return FilterReply.NEUTRAL;
		}
	}
}

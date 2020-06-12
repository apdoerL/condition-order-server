package org.apdoer.condition.quot.process.impl;


import org.apdoer.condition.quot.payload.QuotPriceMessageProcessPayload;
import org.apdoer.condition.quot.payload.QuotPriceMessageSourcePayload;
import org.apdoer.condition.quot.process.Processor;
import org.apdoer.condition.quot.process.assemble.Assembler;
import org.apdoer.condition.quot.process.cleaner.Cleaner;
import org.apdoer.condition.quot.process.filter.Filter;

import java.util.List;


/**
 * 行情快照处理器
 *
 * @author apdoer
 */
public class QuotMessageProcessor implements Processor<QuotPriceMessageSourcePayload, QuotPriceMessageProcessPayload> {


    private List<Cleaner<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload>> cleanerList;

    private List<Filter<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload>> filterList;

    private Assembler<QuotPriceMessageSourcePayload, QuotPriceMessageProcessPayload> assembler;

    public QuotMessageProcessor(List<Cleaner<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload>> cleanerList,
                                List<Filter<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload>> filterList, Assembler<QuotPriceMessageSourcePayload, QuotPriceMessageProcessPayload> assembler) {
        this.cleanerList = cleanerList;
        this.filterList = filterList;
        this.assembler = assembler;
    }


    @Override
    public QuotPriceMessageProcessPayload process(QuotPriceMessageSourcePayload resource) {
        for (Cleaner<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload> cleaner : this.cleanerList) {
            resource = cleaner.clean(resource);
        }
        for (Filter<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload> filter : this.filterList) {
            resource = filter.filter(resource);
        }
        return this.assembler.assemble(resource);
    }
}

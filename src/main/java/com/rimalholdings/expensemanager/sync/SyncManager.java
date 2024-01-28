package com.rimalholdings.expensemanager.sync;

import com.rimalholdings.expensemanager.data.dto.Vendor;
import com.rimalholdings.expensemanager.data.dto.sync.VendorResponse;
import com.rimalholdings.expensemanager.model.mapper.VendorServiceMapper;
import com.rimalholdings.expensemanager.queue.RabbitMQConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j(topic = "SyncManager")
public class SyncManager<T> {
    private final VendorServiceMapper vendorServiceMapper;

    public SyncManager(VendorServiceMapper vendorServiceMapper) {
        this.vendorServiceMapper = vendorServiceMapper;
    }


    public void sync(MessageWrapper<T> messageWrapper) {
        String entityName = messageWrapper.getEntityName();
        List<T> message = messageWrapper.getMessage();

        switch (entityName) {
            case "vendors":
                 vendorServiceMapper.saveVendors((List<Vendor>)message);
                break;
            case "purchaseInvoices":
                log.info("Purchase Invoices");
                break;
            default:
                log.info("No entity found");
        }
    }

}

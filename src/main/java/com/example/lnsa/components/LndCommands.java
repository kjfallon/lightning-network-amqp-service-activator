package com.example.lnsa.components;

import org.lightningj.lnd.wrapper.StatusException;
import org.lightningj.lnd.wrapper.SynchronousLndAPI;
import org.lightningj.lnd.wrapper.ValidationException;
import org.lightningj.lnd.wrapper.message.DeleteAllPaymentsResponse;
import org.lightningj.lnd.wrapper.message.FeeReportResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

@Component("lndCommands")
public class LndCommands {

    private static final Logger log = LoggerFactory.getLogger(LndCommands.class);

    @Resource(name = "encryptedProperties")
    Properties encryptedProperties;

    @Autowired
    SynchronousLndAPI syncLnd;

    // process command literals from message payload or parse message payload for command information
    // for the message payload to be parsed by the consumer as a string like in the two examples below a messsage
    // property must have a name of content_type and a value of text/plain

    public Boolean receivedCommand(String rawCommand) {

        Boolean commandSuccess = false;

        switch (rawCommand) {
            case "feeReport":
                commandSuccess = commandFeeReport(rawCommand);
                break;
            case "deleteAllPayments":
                commandSuccess = commandDeleteAllPayments(rawCommand);
                break;

            default:
                commandSuccess = parseCommand(rawCommand);
        }

        log.info("Command execution complete");
        return commandSuccess;
    }

    private Boolean commandFeeReport(String command) {

        Boolean commandSuccess = false;
        log.debug("Executing command feeReport");
        FeeReportResponse response = null;
        try {
            response = syncLnd.feeReport();
        } catch (StatusException e) {
            log.error("StatusException Error executing feeReport");
            e.printStackTrace();
        } catch (ValidationException e) {
            log.error("ValidationException Error executing feeReport");
            e.printStackTrace();
        }
        if (response != null) {
            commandSuccess = true;
            log.info("Command result: " + response.toJsonAsString(false));
        }

        return commandSuccess;
    }

    private Boolean commandDeleteAllPayments(String command) {

        Boolean commandSuccess = false;
        log.debug("Executing command deleteAllPayments");
        DeleteAllPaymentsResponse response = null;
        try {
            response = syncLnd.deleteAllPayments();
        } catch (StatusException e) {
            log.error("StatusException Error executing deleteAllPayments");
            e.printStackTrace();
        } catch (ValidationException e) {
            log.error("ValidationException Error executing deleteAllPayments");
            e.printStackTrace();
        }
        if (response != null) {
            commandSuccess = true;
            log.info("Command result: " + response.toJsonAsString(false));
        }

        return commandSuccess;
    }

    private Boolean parseCommand(String command) {

        Boolean commandSuccess = false;

        //TODO
        // validate sig, parse parameters, etc.
        // then delegate commands

        log.info("Command unknown");

        return commandSuccess;
    }

}

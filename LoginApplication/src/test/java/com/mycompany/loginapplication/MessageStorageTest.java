/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.loginapplication;


import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



import java.util.Arrays;

/**
 *
 * @author Student
 */

public class MessageStorageTest {

    @BeforeEach
    void reset() {
        MessageStorageManager.sentCount = 0;
        MessageStorageManager.disregardedCount = 0;
        MessageStorageManager.storedCount = 0;
        Arrays.fill(MessageStorageManager.sentMessages, null);
        Arrays.fill(MessageStorageManager.disregardedMessages, null);
        Arrays.fill(MessageStorageManager.storedMessages, null);
        Arrays.fill(MessageStorageManager.messageHashes, null);
    }

    @Test
    void test01_listsEmptyAtStart() {
        assertEquals(0, MessageStorageManager.sentCount);
        assertEquals(0, MessageStorageManager.disregardedCount);
        assertEquals(0, MessageStorageManager.storedCount);
    }

    @Test
    void test02_routeSentMessage() {
        Message m = new Message();
        m.setStatus("Sent");
        MessageStorageManager.routeMessage(m);
        assertEquals(1, MessageStorageManager.sentCount);
    }

    @Test
    void test03_routeDisregardedMessage() {
        Message m = new Message();
        m.setStatus("Disregarded");
        MessageStorageManager.routeMessage(m);
        assertEquals(1, MessageStorageManager.disregardedCount);
    }

    @Test
    void test04_routeStoredMessage() {
        Message m = new Message();
        m.setStatus("Stored");
        m.setMessageHash("hash04");
        MessageStorageManager.routeMessage(m);
        assertEquals(1, MessageStorageManager.storedCount);
    }

    @Test
    void test05_routeNullSafe() {
        MessageStorageManager.routeMessage(null);
        assertEquals(0, MessageStorageManager.sentCount);
        assertEquals(0, MessageStorageManager.disregardedCount);
        assertEquals(0, MessageStorageManager.storedCount);
    }

    @Test
    void test06_displayRecipientsEmpty() {
        assertDoesNotThrow(() -> MessageStorageManager.displayAllRecipients());
    }

    @Test
    void test07_displayRecipientsWithMessages() {
        addStored("001", "+27831234567", "Hi", "h001");
        addStored("002", "+27839876543", "Bye", "h002");
        assertDoesNotThrow(() -> MessageStorageManager.displayAllRecipients());
    }

    @Test
    void test08_displayLongestEmpty() {
        assertDoesNotThrow(() -> MessageStorageManager.displayLongestMessage());
    }

    @Test
    void test09_displayLongestMultiple() {
        addStored("A1", "+27830000001", "Short", "h1");
        addStored("A2", "+27830000002", "Much longer message here", "h2");
        assertDoesNotThrow(() -> MessageStorageManager.displayLongestMessage());
    }

    @Test
    void test10_displayLongestSingle() {
        addStored("S1", "+27830000001", "Only one", "h1");
        assertDoesNotThrow(() -> MessageStorageManager.displayLongestMessage());
    }

    @Test
    void test11_searchByIdFound() {
        addStored("ABC123", "+27831112223", "Found me", "h1");
        assertDoesNotThrow(() -> MessageStorageManager.searchByMessageId("ABC123"));
    }

    @Test
    void test12_searchByIdNotFound() {
        addStored("00001", "+27831112223", "Not me", "h1");
        assertDoesNotThrow(() -> MessageStorageManager.searchByMessageId("ZZZZZ"));
    }

    @Test
    void test13_searchByRecipientFound() {
        addStored("M1", "+27835556667", "First", "h1");
        addStored("M2", "+27835556667", "Second", "h2");
        assertDoesNotThrow(() -> MessageStorageManager.searchByRecipient("+27835556667"));
    }

    @Test
    void test14_searchByRecipientNone() {
        addStored("M1", "+27831234567", "Test", "h1");
        assertDoesNotThrow(() -> MessageStorageManager.searchByRecipient("+27999888776"));
    }

    @Test
    void test15_deleteByHashSuccess() {
        Message m = new Message();
        m.setMessageID("D1");
        m.setRecipientCell("+27831112223");
        m.setMessageText("Delete me");
        m.setStatus("Stored");
        m.setMessageHash("del99");
        MessageStorageManager.routeMessage(m);

        assertEquals(1, MessageStorageManager.storedCount, "Before delete");
        assertEquals("del99", MessageStorageManager.messageHashes[0], "Hash before delete");
        
        MessageStorageManager.deleteByHash("del99");
        
        assertEquals(0, MessageStorageManager.storedCount, "After delete");
    }

    @Test
    void test16_deleteByHashWrong() {
        addStored("K1", "+27831112223", "Keep me", "keep1");
        int count = MessageStorageManager.storedCount;
        MessageStorageManager.deleteByHash("wrongHash");
        assertEquals(count, MessageStorageManager.storedCount);
    }

    @Test
    void test17_fullReportEmpty() {
        assertDoesNotThrow(() -> MessageStorageManager.displayFullReport());
    }

    @Test
    void test18_fullReportWithData() {
        addSent("S1", "+27831111111", "Sent text");
        addDisregarded("D1", "Discarded text");
        addStored("T1", "+27832222222", "Stored text", "h1");
        assertDoesNotThrow(() -> MessageStorageManager.displayFullReport());
    }

    @Test
    void test19_menuMethodExists() {
        assertDoesNotThrow(() ->
            MessageStorageManager.class.getMethod("showStoredMessagesMenu", java.util.Scanner.class)
        );
    }

    @Test
    void test20_listsIndependent() {
        for (int i = 1; i <= 3; i++) {
            addSent("S"+i, "+2783000000"+i, "Hi");
            addDisregarded("D"+i, "Bye");
            addStored("T"+i, "+2783999999"+i, "Save", "h"+i);
        }
        assertEquals(3, MessageStorageManager.sentCount);
        assertEquals(3, MessageStorageManager.disregardedCount);
        assertEquals(3, MessageStorageManager.storedCount);
    }

    private void addStored(String id, String cell, String text, String hash) {
        Message m = new Message();
        m.setMessageID(id);
        m.setRecipientCell(cell);
        m.setMessageText(text);
        m.setStatus("Stored");
        m.setMessageHash(hash);
        MessageStorageManager.routeMessage(m);
    }

    private void addSent(String id, String cell, String text) {
        Message m = new Message();
        m.setMessageID(id);
        m.setRecipientCell(cell);
        m.setMessageText(text);
        m.setStatus("Sent");
        MessageStorageManager.routeMessage(m);
    }

    private void addDisregarded(String id, String text) {
        Message m = new Message();
        m.setMessageID(id);
        m.setMessageText(text);
        m.setStatus("Disregarded");
        MessageStorageManager.routeMessage(m);
    }
}


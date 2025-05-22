package org.velihangozek.javarentalcli;


import org.velihangozek.javarentalcli.util.DBConnection;
import org.velihangozek.javarentalcli.util.PasswordUtil;

public class VeloRentalMain {
    public static void main(String[] args) throws Exception {
        try (var conn = DBConnection.getConnection()) {
            System.out.println("Connected! " + conn.getMetaData().getURL());
        }

        System.out.println(PasswordUtil.hash("secret123"));
    }
}
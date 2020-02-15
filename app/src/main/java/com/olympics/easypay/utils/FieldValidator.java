package com.olympics.easypay.utils;

import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

public class FieldValidator {
    public static boolean checkEmail(TextInputLayout emailEdt) {
        boolean b = true;
        String email = emailEdt.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            emailEdt.setError("Enter your email");
            b = false;
        } else {
            if (!email.endsWith("@gmail.com") && !email.endsWith("@Gmail.com") && !email.endsWith("@outlook.com") && !email.endsWith("@Outlook.com") && !email.endsWith("@yahoo.com") && !email.endsWith("@Yahoo.com")) {
                emailEdt.setError("Email must follow this format\n(example@extension.com)");
                b = false;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEdt.setError("Email must follow this format\n(example@extension.com)");
                b = false;
            }
        }
        if (b) emailEdt.setError(null);
        return b;
    }

    public static boolean checkName(TextInputLayout nameEdt) {
        boolean b = true;
        String name = nameEdt.getEditText().getText().toString().trim();
        if (name.isEmpty()) {
            nameEdt.setError("Enter your name");
            b = false;
        }
        if (b) nameEdt.setError(null);
        return b;
    }

    public static boolean checkPhone(TextInputLayout phoneEdt) {
        boolean b = true;
        String phone = phoneEdt.getEditText().getText().toString().trim();
        if (phone.isEmpty()) {
            phoneEdt.setError("Enter your phone");
            b = false;
        } else {
            if (phone.length() < 11) {
                phoneEdt.setError("Phone must be 11 digits");
                b = false;
            } else {
                if (!phone.startsWith("010") && !phone.startsWith("011") && !phone.startsWith("012")) {
                    phoneEdt.setError("Phone must start with or 010, 011 or 012");
                    b = false;
                } else {
                    if (!Patterns.PHONE.matcher(phone).matches()) {
                        phoneEdt.setError("Phone badly formatted");
                        b = false;
                    }
                }
            }
        }
        if (b) phoneEdt.setError(null);
        return b;
    }

    public static boolean checkPassword(TextInputLayout passEdt) {
        boolean b = true;
        String pass = passEdt.getEditText().getText().toString().trim();
        if (pass.isEmpty()) {
            passEdt.setError("Enter password");
            b = false;
        } else {
            String s = "Password must contain";
            int errors = 0;
            if (!Constants.PATTERN_pass_digits.matcher(pass).matches()) {
                s = s + " ";
                s = s + "digits";
                errors++;
                b = false;
            }
            if (!Constants.PATTERN_pass_lowercase.matcher(pass).matches()) {
                if (errors > 0) {
                    s = s + ", ";
                } else {
                    s = s + " ";
                }
                s = s + "lowercase letters";
                errors++;
                b = false;
            }
            if (!Constants.PATTERN_pass_uppercase.matcher(pass).matches()) {
                if (errors > 0) {
                    s = s + ", ";
                } else {
                    s = s + " ";
                }
                s = s + "uppercase letters";
                errors++;
                b = false;
            }
            if (!Constants.PATTERN_pass_special.matcher(pass).matches()) {
                if (errors > 0) {
                    s = s + ", ";
                } else {
                    s = s + " ";
                }
                s = s + "special characters";
                errors++;
                b = false;
            }
            if (Constants.PATTERN_pass_space.matcher(pass).matches()) {
                if (errors > 0) {
                    s = s + ", ";
                } else {
                    s = s + " ";
                }
                s = s + "no spaces";
                errors++;
                b = false;
            }
            if (!Constants.PATTERN_pass_length.matcher(pass).matches()) {
                if (errors > 0) {
                    s = s + ", ";
                } else {
                    s = s + " ";
                }
                s = s + "8 characters";
                errors++;
                b = false;
            }
            s = s + ".";
            if (errors > 0) {
                passEdt.setError(s);
            }
        }
        if (b) passEdt.setError(null);
        return b;
    }

    public static boolean checkRePassword(TextInputLayout rePassEdt, TextInputLayout passEdt) {
        boolean b = true;
        String pass = passEdt.getEditText().getText().toString().trim();
        String repass = rePassEdt.getEditText().getText().toString().trim();
        if (repass.isEmpty()) {
            rePassEdt.setError("Confirm Password");
            b = false;
        } else if (!repass.equals(pass)) {
            rePassEdt.setError("Password Mismatch");
            b = false;
        }
        if (b) rePassEdt.setError(null);
        return b;
    }
}

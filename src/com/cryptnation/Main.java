package com.cryptnation;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.passay.DigestDictionaryRule.ERROR_CODE;
import static picocli.CommandLine.Help.Ansi.AUTO;

@Command(
        name = "pass",
        mixinStandardHelpOptions = true,
        version = "1.0",
        headerHeading = "@|bold,underline Usage|@:%n",
        synopsisHeading = "%n@|bold,underline Synopsis|@:%n",
        descriptionHeading = "%n@|bold,underline Description|@:%n",
        parameterListHeading = "%n@|bold,underline Parameters|@:%n",
        optionListHeading = "%n@|bold,underline Options|@:%n",
        commandListHeading = "%n@|bold,underline Commands|@:%n",
        header = "@|bold,bg(white),fg(blue) generates passwords, validates passwords" +
                " and hashes files or strings|@",
        description = "@|bold,fg(magenta) generates random complex passwords" +
                ", validates that given passwords are complex enough and " +
                "unbreakable and hashes files or strings|@",
        subcommands = {Eval.class, Hash.class}
)
public class Main implements Callable<Integer> {

    @Parameters(index = "0", defaultValue = "10",
            description = "length of the generated password," +
                    " @|italic,blink,fg(red) default is 10|@")
    private int length;

    @Option(names = {"--no-upper"}, negatable = true, description = "upper case in password, @|italic,blink,fg(red) default is true|@")
    private boolean upper;

    @Option(names = {"--no-lower"}, negatable = true, description = "lower case in password, @|italic,blink,fg(red) default is true|@")
    private boolean lower;

    @Option(names = {"--no-special"}, negatable = true, description = "special characters in password, @|italic,blink,fg(red) default is true|@")
    private boolean special;

    @Option(names = {"--no-digit"}, negatable = true, description = "digits in password, @|italic,blink,fg(red) default is true|@")
    private boolean digit;


    public static void main(String... args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    public String genPassword() {
        PasswordGenerator gen = new PasswordGenerator();

        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);


        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return ERROR_CODE;
            }

            public String getCharacters() {
                return "!@#$%^&*()_+{}[]?<>';:";
            }
        };

        CharacterRule splCharRule = new CharacterRule(specialChars);

        lowerCaseRule.setNumberOfCharacters(2);
        upperCaseRule.setNumberOfCharacters(2);
        digitRule.setNumberOfCharacters(2);
        splCharRule.setNumberOfCharacters(2);

        List<CharacterRule> arr = new ArrayList<>();
        if (!upper) arr.add(upperCaseRule);
        if (!lower) arr.add(lowerCaseRule);
        if (!digit) arr.add(digitRule);
        if (!special) arr.add(splCharRule);

        return gen.generatePassword(length, arr);
    }

    @Override
    public Integer call() throws Exception {
        String str = AUTO.string("@|bold,yellow " + genPassword() + "|@");
        System.out.println(str);
        return 0;
    }
}


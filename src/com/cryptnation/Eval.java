package com.cryptnation;

import org.passay.*;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

import static picocli.CommandLine.Help.Ansi.AUTO;

@Command(
        name = "eval",
        mixinStandardHelpOptions = true,
        version = "1.0",
        headerHeading = "@|bold,underline Usage|@:%n",
        synopsisHeading = "%n@|bold,underline Synopsis|@:%n",
        descriptionHeading = "%n@|bold,underline Description|@:%n",
        parameterListHeading = "%n@|bold,underline Parameters|@:%n",
        optionListHeading = "%n@|bold,underline Options|@:%n",
        commandListHeading = "%n@|bold,underline Commands|@:%n",
        header = "@|bold,bg(white),fg(blue) validates security of passwords|@",
        description = "@|fg(magenta) validates complexities of passwords and verifies" +
                " that they are unbreakable|@"
)
class Eval implements Callable<Integer> {

    @Option(names = {"-p", "--password"}, required = true, interactive = true,
            description = "the password to be validated, @|italic,blink,fg(red) required|@")
    char[] password;

    @Override
    public Integer call() throws Exception {
        validatePassword();
        return 0;
    }

    public void validatePassword() {
        Rule rule1 = new LengthRule(8, 30);
        Rule rule2 = new WhitespaceRule();
        CharacterCharacteristicsRule rule3 = new CharacterCharacteristicsRule();

        rule3.setNumberOfCharacteristics(3);
        rule3.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 2));
        rule3.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 2));
        rule3.getRules().add(new CharacterRule(EnglishCharacterData.Digit, 2));
        rule3.getRules().add(new CharacterRule(EnglishCharacterData.Special, 2));

        PasswordValidator validator = new PasswordValidator(rule1, rule2, rule3);
        PasswordData data = new PasswordData(String.valueOf(password));
        RuleResult result = validator.validate(data);

        if (result.isValid()) {
            String str = AUTO.string("@|bold,blink,green you are UNBREAKABLE!|@");
            System.out.println(str);
        } else {
            String str = AUTO.string("@|bold,red " + validator.getMessages(result) + "|@");
            System.out.println("Invalid Password: " + str);
        }
    }
}

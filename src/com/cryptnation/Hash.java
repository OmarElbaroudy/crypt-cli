package com.cryptnation;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.Callable;

@Command(
        name = "hash",
        mixinStandardHelpOptions = true,
        version = "1.0",
        headerHeading = "@|bold,underline Usage|@:%n",
        synopsisHeading = "%n@|bold,underline Synopsis|@:%n",
        descriptionHeading = "%n@|bold,underline Description|@:%n",
        parameterListHeading = "%n@|bold,underline Parameters|@:%n",
        optionListHeading = "%n@|bold,underline Options|@:%n",
        commandListHeading = "%n@|bold,underline Commands|@:%n",
        header = "@|bold,bg(white),fg(blue) hashes given file or string|@",
        description = "@|fg(magenta) hashes the input file or string to hex using SHA256|@"
)
class Hash implements Callable<Integer> {

    @ArgGroup(multiplicity = "1")
    ExclusiveHashOptions options;

    @Option(names = {"-e", "--encode"}, description = "encode to base64")
    boolean encode;

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    public Integer call() throws Exception {
        byte[] arr = (Objects.nonNull(options.file))
                ? Files.readAllBytes(options.file.toPath())
                : options.str.getBytes(StandardCharsets.UTF_8);

        System.out.println(encode
                ? Base64.getEncoder().encodeToString(hash(arr).getBytes())
                : hash(arr));

        return 0;
    }

    private String hash(byte[] arr) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(arr);
        return bytesToHex(md.digest());
    }
}

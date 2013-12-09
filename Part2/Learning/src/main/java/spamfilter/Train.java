package spamfilter;

import classification.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Train {
    private static final int NO_FOLDS = 10;

    public static void usage() {
        System.out.println("Usage: ");
        System.out.println("\tjava -jar spamfilter-learning.jar <traindata> <outfile>");
    }

    public static void testClassifier(String trainingPath, EmailClassifier emailClassifier) throws IOException {
        CrossValidation cv = new CrossValidation(trainingPath, emailClassifier);
        cv.fold(NO_FOLDS);
        cv.getCombinedConfusion().print();
        System.out.format("StdDev: %f\n", cv.getStdDev());
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            usage();
            return;
        }

        String stateFilePath = args[1];
        String trainingPath = args[0];

        FeatureWeighting weightingMethod;
        //weightingMethod = new TfidfWeighting();
        weightingMethod = new FrequencyWeighting();

        // Classifier classifier = new J48()
        Classifier classifier = new NaiveBayes();
        EmailClassifier emailClassifier = new EmailClassifier(classifier, weightingMethod, true, true);
        //emailClassifier.getParser().setSeparateMetadata(false);
        //emailClassifier.getParser().setSplitMultipart(false);
        //emailClassifier.getParser().setStripHtml(false);

        System.out.format("Performing cross-validation on %d folds...\n", NO_FOLDS);
        testClassifier(trainingPath, emailClassifier);

        System.out.println("Finished Testing, performing final train step...");
        File trainingDir = new File(trainingPath);

        emailClassifier.train(Arrays.asList(trainingDir.listFiles()));

        System.out.format("Feature Dimensions = %d\n", emailClassifier.getTermCount());

        EmailClassifier.save(emailClassifier, stateFilePath);
        System.out.format("Saved model to %s\n", stateFilePath);
    }
}

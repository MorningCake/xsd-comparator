package ru.alfabank.epk.reactive.ok;

public class ParserMain {
    public static void main(String[] args) {
        SaxXsdReader saxReader = new SaxXsdReader();
        saxReader.readXsd("UWSConsumerSubjectInfoGetInOutParms28_CHANGES_FOR_DIFF.xsd",
                true, true, true, true, false, "28", false);

    }
}

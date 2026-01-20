package ru.alfabank.epk.reactive;

import java.util.Set;

public class Main {

        public static void main(String[] args) throws Exception {
            XPathExporter xPathExporter = new XPathExporter("client_app.xsd", Set.of(
                    "GetRequest",
                    "GetResponse",
                    "GetGoldRecRequest",
                    "GetGoldRecResponse",
                    "GetListSubRelRequest",
                    "GetListSubRelResponse",
                    "GetListRequest",
                    "GetListResponse"
            ));
            xPathExporter.xsdToCsv();

            XPathExporter xPathExporter2 = new XPathExporter(
                    "UWSConsumerSubjectInfoGetInOutParms28.xsd",
                    Set.of(
                            "UWSConsumerSubjectInfoGetGetInParms" // TODO
                    )
            );
            xPathExporter2.xsdToCsv();
        }
}

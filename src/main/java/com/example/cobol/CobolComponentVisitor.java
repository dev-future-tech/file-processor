package com.example.cobol;

import com.khubla.antlr4example.Cobol85BaseVisitor;
import com.khubla.antlr4example.Cobol85Parser;

public class CobolComponentVisitor extends Cobol85BaseVisitor<CobolItem> {

    @Override
    public CobolItem visitIdentificationDivision(Cobol85Parser.IdentificationDivisionContext ctx) {
        return super.visitIdentificationDivision(ctx);
    }
}

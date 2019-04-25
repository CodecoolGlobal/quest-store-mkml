package com.queststore.Controller;

import com.queststore.DAO.DaoException;
import com.queststore.Model.Card;
import com.queststore.Model.CardTypes;
import com.queststore.Model.User;
import com.queststore.Services.CardService;
import org.jtwig.JtwigModel;

import java.util.ArrayList;
import java.util.List;

public class StudentArtifactStore extends Student {

    private CardService cardService;

    public StudentArtifactStore(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    String getTemplatePath() {
        return "/templates/student/student_artifact_store.twig";
    }

    @Override
    JtwigModel getModelWithBodyFilled(User user) {
        List<Card> artifacts = new ArrayList<>();

        try {
            artifacts.addAll(cardService.getAllCards(new CardTypes(2, "artifact")));
        } catch (DaoException e) {
            e.printStackTrace();
        }

        JtwigModel model = JtwigModel.newModel();
        model.with("artifactList", artifacts);

        return model;
    }
}

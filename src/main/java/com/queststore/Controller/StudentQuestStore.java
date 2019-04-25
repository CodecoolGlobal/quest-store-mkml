package com.queststore.Controller;

import com.queststore.DAO.DaoException;
import com.queststore.Model.Card;
import com.queststore.Model.CardTypes;
import com.queststore.Model.User;
import com.queststore.Services.CardService;
import org.jtwig.JtwigModel;

import java.util.ArrayList;
import java.util.List;

public class StudentQuestStore extends Student {

    private CardService cardService;

    public StudentQuestStore(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    String getTemplatePath() {
        return "/templates/student/student_quest_store.twig";
    }

    @Override
    JtwigModel getModelWithBodyFilled(User user) {

        List<Card> quests = new ArrayList<>();
        try {
            quests.addAll(cardService.getAllCards(new CardTypes(1, "quest")));
        } catch (DaoException e) {
            e.printStackTrace();
        }

        JtwigModel model = JtwigModel.newModel();
        model.with("questList", quests);

        return model;
    }
}

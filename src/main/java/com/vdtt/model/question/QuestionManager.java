package com.vdtt.model.question;

import com.vdtt.util.Util;

import java.util.ArrayList;
import java.util.List;

public class QuestionManager {

    public static final QuestionManager instance = new QuestionManager();


    private List<Question> questions;

    private QuestionManager() {
        this.questions = new ArrayList<>();
        init();
    }

    public void init() {
        String[] QUESTIONS = {
                "Ai là người Saiyan mạnh nhất?;Goku,Vegeta,Broly,Gohan#0",
                "Ai là đệ tử của Quy lão tiên sinh?;Yamcha,Goku,Krillin,Chi-Chi#1",
                "Ai là cha của Goku?;Bardock,King Vegeta,Raditz,Nappa#0",
                "Ai là vợ của Goku?;Chi-Chi,Bulma,Videl,Launch#0",
                "Ai là kẻ thù đầu tiên của Goku?;Piccolo,Raditz,Frieza,Cell#0",
                "Ai là người tạo ra Dragon Balls?;Kami,Dende,Piccolo,King Kai#0",
                "Ai là người mạnh nhất trong vũ trụ 7?;Beerus,Whis,Goku,Vegeta#1",
                "Ai là kẻ thù cuối cùng trong Dragon Ball Z?;Majin Buu,Cell,Frieza,Vegeta#0",
                "Ai là người chiến thắng giải đấu quyền lực?;Goku,Jiren,Frieza,Android 17#3",
                "Ai là người sáng tạo ra Capsule Corp?;Bulma,Dr. Briefs,Vegeta,Trunks#1",
                "Ai là con trai đầu lòng của Goku?;Gohan,Goten,Trunks,Goku Jr.#0",
                "Ai là người sở hữu Super Saiyan Blue đầu tiên?;Goku,Vegeta,Trunks,Black Goku#0",
                "Ai là nhân vật nữ chính trong Dragon Ball?;Chi-Chi,Bulma,Videl,Launch#1",
                "Ai là đệ tử của Gohan?;Pan,Trunks,Goten,Videl#0",
                "Ai là người tiêu diệt Cell?;Goku,Vegeta,Gohan,Trunks#2",
                "Ai là kẻ thù chính trong Dragon Ball GT?;Baby,Super 17,Omega Shenron,Dr. Myuu#0",
                "Ai là người phát minh ra máy thời gian?;Bulma,Trunks,Dr. Briefs,Goku#0",
                "Ai là người chiến thắng giải đấu Tenkaichi Budokai đầu tiên?;Goku,Jackie Chun,Krillin,Yamcha#1",
                "Ai là người sở hữu Ultra Instinct?;Goku,Vegeta,Jiren,Frieza#0",
                "Ai là người sáng lập quân đội Red Ribbon?;Commander Red,Dr. Gero,Colonel Silver,General Blue#0",
                "Ai là người Saiyan đầu tiên biến thành Super Saiyan?;Goku,Bardock,Vegeta,Broly#0",
                "Ai là người đầu tiên đạt được trạng thái Super Saiyan God?;Goku,Vegeta,Broly,Trunks#0",
                "Ai là người mạnh nhất trong vũ trụ 6?;Hit,Cabba,Kale,Caulifla#0",
                "Ai là người sáng lập Time Patrol?;Trunks,Goku,Vegeta,Pan#0",
                "Ai là người tiêu diệt Frieza lần đầu tiên?;Goku,Vegeta,Future Trunks,Gohan#2",
                "Ai là người tạo ra Super Dragon Balls?;Zalama,Zeno,Grand Priest,Kami#0",
                "Ai là người đầu tiên đạt trạng thái Super Saiyan 4?;Goku,Vegeta,Gogeta,Broly#0",
                "Ai là kẻ thù chính trong saga Majin Buu?;Majin Buu,Babidi,Dabura,Vegeta#0",
                "Ai là người đứng đầu Galactic Patrol?;Jaco,Merus,King Cold,Granolah#0",
                "Ai là người đánh bại Vegeta lần đầu tiên?;Goku,Gohan,Yajirobe,Krillin#0",
                "Ai là người mạnh nhất trong vũ trụ 11?;Jiren,Toppo,Dispo,Belmod#0",
                "Ai là người sáng lập Saiyan?;Yamoshi,King Vegeta,Bardock,Gine#0",
                "Ai là người sở hữu Kaioken?;Goku,Vegeta,Gohan,Piccolo#0",
                "Ai là người tiêu diệt Kid Buu?;Goku,Vegeta,Gohan,Mr. Satan#0",
                "Ai là kẻ thù chính trong Dragon Ball Super: Broly?;Broly,Paragus,Frieza,Beerus#0",
                "Ai là người sáng lập Capsule Corp?;Dr. Briefs,Bulma,Vegeta,Trunks#0",
                "Ai là người tiêu diệt Android 16?;Cell,Goku,Gohan,Vegeta#0",
                "Ai là người chiến thắng giải đấu Universe 6 và 7?;Goku,Hit,Vegeta,Monaka#1",
                "Ai là người đầu tiên biến thành Super Saiyan 2?;Goku,Gohan,Vegeta,Trunks#1",
                "Ai là người tiêu diệt King Piccolo?;Goku,Krillin,Yamcha,Tien#0",
                "Ai là người tiêu diệt Zamasu?;Goku,Vegeta,Trunks,Beerus#2",
                "Ai là người đạt trạng thái Super Saiyan 3 đầu tiên?;Goku,Vegeta,Gotenks,Trunks#0",
                "Ai là người tiêu diệt Dr. Gero?;Android 17,Goku,Vegeta,Piccolo#0",
                "Ai là người sáng lập Saiyan Armor?;King Vegeta,Paragus,Bardock,Vegeta#0",
                "Ai là người tiêu diệt Nappa?;Vegeta,Goku,Gohan,Krillin#0",
                "Ai là người sáng lập Capsule Corporation?;Dr. Briefs,Bulma,Vegeta,Trunks#0",
                "Ai là người chiến thắng giải đấu Power Tournament?;Goku,Jiren,Frieza,Android 17#3",
                "Ai là người đầu tiên biến thành Super Saiyan Blue?;Goku,Vegeta,Trunks,Black Goku#0",
                // Thêm các câu hỏi khác ở đây
                "Ai là người tiêu diệt Majin Buu?;Goku,Vegeta,Gohan,Mr. Satan#0",
                "Ai là người đầu tiên đạt trạng thái Ultra Instinct?;Goku,Vegeta,Jiren,Frieza#0",
                "Ai là người sáng lập Galactic Patrol?;Jaco,Merus,King Cold,Granolah#0",
                "Ai là người đầu tiên biến thành Super Saiyan?;Goku,Bardock,Vegeta,Broly#0",
                "Ai là kẻ thù đầu tiên của Goku?;Piccolo,Raditz,Frieza,Cell#0",
                "Ai là người mạnh nhất trong vũ trụ 7?;Beerus,Whis,Goku,Vegeta#1",
                "Ai là kẻ thù cuối cùng trong Dragon Ball Z?;Majin Buu,Cell,Frieza,Vegeta#0",
                "Ai là người chiến thắng giải đấu quyền lực?;Goku,Jiren,Frieza,Android 17#3",
                "Ai là người sáng tạo ra Capsule Corp?;Bulma,Dr. Briefs,Vegeta,Trunks#1",
                "Ai là con trai đầu lòng của Goku?;Gohan,Goten,Trunks,Goku Jr.#0",
                "Ai là người sở hữu Super Saiyan Blue đầu tiên?;Goku,Vegeta,Trunks,Black Goku#0",
                "Ai là nhân vật nữ chính trong Dragon Ball?;Chi-Chi,Bulma,Videl,Launch#1",
                "Ai là đệ tử của Gohan?;Pan,Trunks,Goten,Videl#0",
                "Ai là người tiêu diệt Cell?;Goku,Vegeta,Gohan,Trunks#2",
                "Ai là kẻ thù chính trong Dragon Ball GT?;Baby,Super 17,Omega Shenron,Dr. Myuu#0",
                "Ai là người phát minh ra máy thời gian?;Bulma,Trunks,Dr. Briefs,Goku#0",
                "Ai là người chiến thắng giải đấu Tenkaichi Budokai đầu tiên?;Goku,Jackie Chun,Krillin,Yamcha#1",
                "Ai là người sở hữu Ultra Instinct?;Goku,Vegeta,Jiren,Frieza#0",
                "Ai là người sáng lập quân đội Red Ribbon?;Commander Red,Dr. Gero,Colonel Silver,General Blue#0",
                "Ai là người Saiyan đầu tiên biến thành Super Saiyan?;Goku,Bardock,Vegeta,Broly#0",
                "Ai là người đầu tiên đạt được trạng thái Super Saiyan God?;Goku,Vegeta,Broly,Trunks#0",
                "Ai là người mạnh nhất trong vũ trụ 6?;Hit,Cabba,Kale,Caulifla#0",
                "Ai là người sáng lập Time Patrol?;Trunks,Goku,Vegeta,Pan#0",
                "Ai là người tiêu diệt Frieza lần đầu tiên?;Goku,Vegeta,Future Trunks,Gohan#2",
                "Ai là người tạo ra Super Dragon Balls?;Zalama,Zeno,Grand Priest,Kami#0",
                "Ai là người đầu tiên đạt trạng thái Super Saiyan 4?;Goku,Vegeta,Gogeta,Broly#0",
                "Ai là kẻ thù chính trong saga Majin Buu?;Majin Buu,Babidi,Dabura,Vegeta#0",
                "Ai là người đứng đầu Galactic Patrol?;Jaco,Merus,King Cold,Granolah#0",
                "Ai là người đánh bại Vegeta lần đầu tiên?;Goku,Gohan,Yajirobe,Krillin#0",
                "Ai là người mạnh nhất trong vũ trụ 11?;Jiren,Toppo,Dispo,Belmod#0",
                "Ai là người sáng lập Saiyan?;Yamoshi,King Vegeta,Bardock,Gine#0",
                "Ai là người sở hữu Kaioken?;Goku,Vegeta,Gohan,Piccolo#0",
                "Ai là người tiêu diệt Kid Buu?;Goku,Vegeta,Gohan,Mr. Satan#0",
                "Ai là kẻ thù chính trong Dragon Ball Super: Broly?;Broly,Paragus,Frieza,Beerus#0",
                "Ai là người sáng lập Capsule Corp?;Dr. Briefs,Bulma,Vegeta,Trunks#0",
                "Ai là người tiêu diệt Android 16?;Cell,Goku,Gohan,Vegeta#0",
                "Ai là người chiến thắng giải đấu Universe 6 và 7?;Goku,Hit,Vegeta,Monaka#1",
                "Ai là người đầu tiên biến thành Super Saiyan 2?;Goku,Gohan,Vegeta,Trunks#1",
                "Ai là người tiêu diệt King Piccolo?;Goku,Krillin,Yamcha,Tien#0",
                "Ai là người tiêu diệt Zamasu?;Goku,Vegeta,Trunks,Beerus#2",
                "Ai là người đạt trạng thái Super Saiyan 3 đầu tiên?;Goku,Vegeta,Gotenks,Trunks#0",
                "Ai là người tiêu diệt Dr. Gero?;Android 17,Goku,Vegeta,Piccolo#0",
                "Ai là người sáng lập Saiyan Armor?;King Vegeta,Paragus,Bardock,Vegeta#0",
                "Ai là người tiêu diệt Nappa?;Vegeta,Goku,Gohan,Krillin#0",
                "Ai là người sáng lập Capsule Corporation?;Dr. Briefs,Bulma,Vegeta,Trunks#0",
                "Ai là người chiến thắng giải đấu Power Tournament?;Goku,Jiren,Frieza,Android 17#3",
                "Ai là người đầu tiên biến thành Super Saiyan Blue?;Goku,Vegeta,Trunks,Black Goku#0",
                // Thêm các câu hỏi khác ở đây
        };
        for (String question : QUESTIONS) {
            addQuestion(parseQuestion(question));
        }
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }


    private Question parseQuestion(String question) {
        String[] parts = question.split(";");
        String questionText = parts[0];
        String[] options = parts[1].split(",");
        Question q = new Question(questionText);
        for (String option : options) {
            q.addAnswer(new Answer(option));
        }
        int correct = Integer.parseInt(question.split("#")[1]);
        q.setCorrectAnswer(correct);
        return q;
    }

    public Question random() {
        return questions.get(Util.nextInt(questions.size()));
    }
}

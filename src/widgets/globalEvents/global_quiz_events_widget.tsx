import type {GlobalEventQuiz} from "../../entities/globalEvents/types/global_event_types.ts";
import {GlobalQuizCard} from "./global_quiz_card.tsx";

interface Props {
    quizzes: GlobalEventQuiz[];
}

export function GlobalQuizEventsWidget({
                                           quizzes,
                                       }: Props) {

    return (
        <>
            <h2 className="page-title">
                Голосования
            </h2>

            {
                quizzes.length > 0
                    ? (
                        <div className="class-list">
                            {
                                quizzes.map(item => (
                                    <GlobalQuizCard
                                        key={item.id}
                                        item={item}
                                    />
                                ))
                            }
                        </div>
                    )
                    : (
                        <div className="empty-state">
                            Голосований нет
                        </div>
                    )
            }
        </>
    );
}
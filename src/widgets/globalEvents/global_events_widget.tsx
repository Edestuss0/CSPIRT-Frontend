import { useGlobalEvents } from "../../features/globalEvents/hooks/use_global_events";
import {GlobalInfoCard} from "./global_info_card.tsx";
import {GlobalQuizCard} from "./global_quiz_card.tsx";

export function GlobalEventsWidget() {
    const {
        data,
        isLoading,
        error,
    } = useGlobalEvents();

    if (isLoading) {
        return (
            <div className="grid grid--3">
                <div className="skeleton" style={{ height: 160 }} />
                <div className="skeleton" style={{ height: 160 }} />
                <div className="skeleton" style={{ height: 160 }} />
            </div>
        );
    }

    if (error) {
        return (
            <div className="alert alert--danger">
                {error.message}
            </div>
        );
    }

    if (!data) {
        return (
            <div className="empty-state">
                <h2 className="empty-state__title">
                    Глобальные события отсутствуют
                </h2>

                <p className="empty-state__text">
                    Пока нет опубликованных новостей и голосований.
                </p>
            </div>
        );
    }
    return (
        <>
            <h2 className="page-title">
                Новости
            </h2>
            <div className="class-list">
                {
                    data.info_events.map(item => (
                        <GlobalInfoCard
                            key={item.id}
                            item={item}
                        />
                    ))
                }
                {
                    data.quizzes.map(item => (
                        <GlobalQuizCard
                            key={item.id}
                            item={item}
                        />
                    ))
                }
            </div>
        </>
    );
}
import { useState } from "react";
import type {GlobalEventQuiz} from "../../entities/globalEvents/types/global_event_types.ts";
import {useVoteGlobalQuizEvent} from "../../features/globalEvents/hooks/use_vote_global_quiz_event.ts";


interface Props {
    item: GlobalEventQuiz;
}

export function GlobalQuizCard({    
                                   item,
                               }: Props) {

    const [selected, setSelected] = useState<number>();

    const vote = useVoteGlobalQuizEvent();

    const totalVotes =
        item.options.reduce(
            (acc, option) => acc + option.votes,
            0,
        );

    return (
        <div className="class-flat-card">

            <div className="class-flat-card__main">

                <div className="class-flat-card__info">

                    <p className="class-flat-card__title">
                        {item.title}
                    </p>

                    <p className="class-flat-card__subtitle">
                        {item.description}
                    </p>

                </div>

            </div>

            <div className="class-flat-card__meta">

                {
                    item.options.map(option => {

                        const percent =
                            totalVotes === 0
                                ? 0
                                : Math.round(
                                    option.votes /
                                    totalVotes *
                                    100,
                                );

                        return (
                            <label
                                key={option.id}
                                className="radio-option"
                            >
                                <input
                                    type="radio"
                                    checked={
                                        selected === option.id
                                    }
                                    onChange={() =>
                                        setSelected(option.id)
                                    }
                                />

                                <span>
                                    {option.title}
                                </span>

                                <span>
                                    {" "}
                                    {option.votes}
                                    {" "}
                                    ({percent}%)
                                </span>

                            </label>
                        );
                    })
                }

                <button
                    className="btn btn--primary"
                    disabled={
                        selected === undefined ||
                        vote.isPending
                    }
                    onClick={() =>
                        vote.mutate({
                            id: item.id,
                            voteItemId: selected!,
                        })
                    }
                >
                    {
                        vote.isPending
                            ? "Голосование..."
                            : "Проголосовать"
                    }
                </button>

            </div>

        </div>
    );
}
import type {GlobalEventInfo} from "../../entities/globalEvents/types/global_event_types.ts";

interface Props {
    item: GlobalEventInfo;
}

export function GlobalInfoCard({ item }: Props) {

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

        </div>
    );
}
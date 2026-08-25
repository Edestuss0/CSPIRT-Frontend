import type {GlobalEventInfo} from "../../entities/globalEvents/types/global_event_types.ts";
import {GlobalInfoCard} from "./global_info_card.tsx";

interface Props {
    events: GlobalEventInfo[];
}

export function GlobalInfoEventsWidget({
                                           events,
                                       }: Props) {

    return (
        <>
            <h2 className="page-title">
                Новости
            </h2>

            {
                events.length > 0
                    ? (
                        <div className="class-list">
                            {
                                events.map(item => (
                                    <GlobalInfoCard
                                        key={item.id}
                                        item={item}
                                    />
                                ))
                            }
                        </div>
                    )
                    : (
                        <div className="empty-state">
                            Новостей нет
                        </div>
                    )
            }
        </>
    );
}
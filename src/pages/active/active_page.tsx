import {PageHeader} from "../../shared/ui/other/page_header.tsx";
import {TabsSwitcher, type TabsSwitcherItem} from "../../shared/ui/other/tabs_switcher.tsx";
import {useState} from "react";
import {GlobalEventsWidget} from "../../widgets/globalEvents/global_events_widget.tsx";
import {ActiveEventsWidget} from "../../widgets/events/active_events_widget.tsx";
import {EventsWidget} from "../../widgets/events/events_widget.tsx";

type tab = "news" | "active_events" | "events" | "preview"

export function ActivePage() {
    

    const tabs: TabsSwitcherItem<tab>[] = [
        {
            value: "preview",
            label: "Главная",
        },
        {
            value: "active_events",
            label: "Активные мероприятия",
        },
        {
            value: "news",
            label: "Новости",
        },
        {
            value: "events",
            label: "Все мероприятия",
        },
    ]
    
    const [selectedTab, setSelectedTab] = useState<tab>("preview");
    
    return (
        <>
            <main className={"main"}>
                <section className={"page"}>
                    <PageHeader title={"Активности"} description={"Просматривайте мероприятия и новости учебного заведения"} hasBackButton={true}/>

                    <div className="page-spacer"></div>
                    
                    <TabsSwitcher items={tabs} value={selectedTab} onChange={setSelectedTab}/>

                    <div className="page-spacer"></div>

                    {selectedTab === "preview" && (
                        <>
                            <GlobalEventsWidget />
                            <ActiveEventsWidget />
                        </>
                    )}

                    {selectedTab === "active_events" && (
                        <>
                            <ActiveEventsWidget />
                        </>
                    )}

                    {selectedTab === "events" && (
                        <>
                            <EventsWidget/>
                        </>
                    )}

                    {selectedTab === "news" && (
                        <>
                            <GlobalEventsWidget />
                        </>
                    )}
                    
                </section>
            </main>
        </>
    )
}
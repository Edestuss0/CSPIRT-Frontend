import { useClassId } from "../../features/class/hooks/use_class_id.ts";
import { useAuthStore } from "../../features/auth/store/auth_store.ts";
import { InfoRow } from "../../pages/profile/ui/profile_page.tsx";

export function MyClassWidget() {
    const user = useAuthStore((state) => state.user?.User);
    const { data, isLoading } = useClassId(user?.ClassID);
    
    if (!user || isLoading) {
        return (
            <div className="grid grid--3">
                <div className="skeleton" style={{ height: 160 }} />
                <div className="skeleton" style={{ height: 160 }} />
                <div className="skeleton" style={{ height: 160 }} />
            </div>
        );
    }

    const totalRating = (data?.ClassTotalRating ?? 0) + (data?.UserTotalRating ?? 0);

    return (
        <>
            <h2 className="page-title">
                Мой класс
            </h2>
            
            <div className="card card--padded">
                <div className="info-list">
                    <InfoRow label="Мой рейтинг" value={user.Rating}/>
                    <InfoRow label="Рейтинг моего класса" value={totalRating}/>
                    <InfoRow label="Рейтинг моего класса" value={totalRating}/>
                    <InfoRow label="Рейтинг учеников моего класса" value={data?.UserTotalRating ?? 0}/>
    
                    <p className="class-flat-card__title">
                        Мой класс в кубке школы
                    </p>
    
                    <InfoRow label="Кол-во первых мест в кубке школы" value={(data?.FirstQuarterComplete ?? 0).toString()}/>
                    <InfoRow label="Кол-во вторых мест в кубке школы" value={(data?.SecondQuarterComplete ?? 0).toString()}/>
                    <InfoRow label="Кол-во третьих мест в кубке школы" value={(data?.ThirdQuarterComplete ?? 0).toString()}/>
                </div>
            </div>
        </>
    );
}
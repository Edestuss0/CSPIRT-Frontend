export type PublicLists = "schedule" | "parallels";

export let CurrentPublicList = "schedule";

export function changePublicList(newList: PublicLists) {CurrentPublicList = newList} 
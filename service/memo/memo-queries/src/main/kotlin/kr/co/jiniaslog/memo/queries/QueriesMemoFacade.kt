package kr.co.jiniaslog.memo.queries

interface QueriesMemoFacade :
    IGetAllMemos,
    IRecommendRelatedMemo,
    IGetMemoById,
    IGetAllReferencesByMemo,
    IGetAllReferencedByMemo,
    ICheckMemoExisted

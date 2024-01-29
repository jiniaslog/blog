package kr.co.jiniaslog.memo.usecase

interface UseCasesFolderFacade :
    ICreateNewFolder,
    IChangeFolderName,
    IMakeRelationShipFolderAndFolder,
    IDeleteFoldersRecursively

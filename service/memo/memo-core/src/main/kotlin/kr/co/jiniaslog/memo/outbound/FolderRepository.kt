package kr.co.jiniaslog.memo.outbound

import kr.co.jiniaslog.memo.domain.folder.Folder
import kr.co.jiniaslog.memo.domain.folder.FolderId
import kr.co.jiniaslog.shared.core.domain.Repository

interface FolderRepository : Repository<Folder, FolderId>

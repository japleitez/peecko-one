import { IPlayList } from 'app/entities/play-list/play-list.model';
import { FieldAccess } from '../../shared/profile/view.models';

export interface IVideoItem {
  id: number;
  previous?: string | null;
  code?: string | null;
  next?: string | null;
  playList?: IPlayList | null;
}

export type NewVideoItem = Omit<IVideoItem, 'id'> & { id: null };

export interface VideoItemAccess {
  id: FieldAccess;
  previous: FieldAccess;
  code: FieldAccess;
  next: FieldAccess;
  playList: FieldAccess;
}

export let VIDEO_ITEM_ACCESS: VideoItemAccess;

VIDEO_ITEM_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  previous: { listable: true, visible: true, disabled: false },
  code: { listable: true, visible: true, disabled: false },
  next: { listable: true, visible: true, disabled: false },
  playList: { listable: true, visible: true, disabled: false },
};

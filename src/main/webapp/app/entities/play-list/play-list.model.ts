import dayjs from 'dayjs/esm';
import { IVideoItem } from 'app/entities/video-item/video-item.model';
import { IApsUser } from 'app/entities/aps-user/aps-user.model';
import { FieldAccess } from '../../shared/profile/view.models';

export interface IPlayList {
  id: number;
  name?: string | null;
  counter?: number | null;
  created?: dayjs.Dayjs | null;
  updated?: dayjs.Dayjs | null;
  videoItems?: IVideoItem[] | null;
  apsUser?: IApsUser | null;
}

export type NewPlayList = Omit<IPlayList, 'id'> & { id: null };

export interface PlayListAccess {
  id: FieldAccess;
  name: FieldAccess;
  counter: FieldAccess;
  created: FieldAccess;
  updated: FieldAccess;
  videoItems: FieldAccess;
  apsUser: FieldAccess;
}

export let PLAYLIST_ACCESS: PlayListAccess;

PLAYLIST_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  name: { listable: true, visible: true, disabled: false },
  counter: { listable: true, visible: true, disabled: false },
  created: { listable: true, visible: true, disabled: true },
  updated: { listable: true, visible: true, disabled: true },
  videoItems: { listable: false, visible: true, disabled: true },
  apsUser: { listable: true, visible: true, disabled: false },
};

import dayjs from 'dayjs/esm';
import { IApsDevice } from 'app/entities/aps-device/aps-device.model';
import { IPlayList } from 'app/entities/play-list/play-list.model';
import { Language } from 'app/entities/enumerations/language.model';
import { FieldAccess } from '../../shared/profile/view.models';

export interface IApsUser {
  id: number;
  name?: string | null;
  username?: string | null;
  usernameVerified?: boolean | null;
  privateEmail?: string | null;
  privateVerified?: boolean | null;
  language?: keyof typeof Language | null;
  license?: string | null;
  active?: boolean | null;
  password?: string | null;
  created?: dayjs.Dayjs | null;
  updated?: dayjs.Dayjs | null;
  apsDevices?: IApsDevice[] | null;
  playLists?: IPlayList[] | null;
}

export type NewApsUser = Omit<IApsUser, 'id'> & { id: null };

export interface ApsUserAccess {
  id: FieldAccess;
  name: FieldAccess;
  username: FieldAccess;
  usernameVerified: FieldAccess;
  privateEmail: FieldAccess;
  privateVerified: FieldAccess;
  language: FieldAccess;
  license: FieldAccess;
  active: FieldAccess;
  password: FieldAccess;
  created: FieldAccess;
  updated: FieldAccess;
  apsDevices: FieldAccess;
  playLists: FieldAccess;
}

export let APS_USER_ACCESS: ApsUserAccess;

APS_USER_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  name: { listable: true, visible: true, disabled: false },
  username: { listable: true, visible: true, disabled: false },
  usernameVerified: { listable: true, visible: true, disabled: false },
  privateEmail: { listable: true, visible: true, disabled: false },
  privateVerified: { listable: true, visible: true, disabled: false },
  language: { listable: true, visible: true, disabled: false },
  license: { listable: true, visible: true, disabled: false },
  active: { listable: true, visible: true, disabled: false },
  password: { listable: false, visible: true, disabled: true },
  created: { listable: true, visible: true, disabled: true },
  updated: { listable: false, visible: true, disabled: true },
  apsDevices: { listable: false, visible: true, disabled: true },
  playLists: { listable: false, visible: true, disabled: true }
}

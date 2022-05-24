import {Role, SimpleScript} from '../../../shared/dtos/script-dtos';
import {BehaviorSubject, Observable} from 'rxjs';

/** Local service for viewing or editing scripts. */
export class ScriptViewerService {
  private isEditing = false;
  private isEditingSubject = new BehaviorSubject<boolean>(this.isEditing);
  private isUploading = false;
  private isUploadingSubject = new BehaviorSubject<boolean>(this.isUploading);
  private script: SimpleScript = null;
  private scriptSubject = new BehaviorSubject<SimpleScript>(this.script);
  private selectedRole: Role = null;
  private selectedRoleSubject = new BehaviorSubject<Role>(this.selectedRole);

  get $selectedRole(): Observable<Role> {
    return this.selectedRoleSubject.asObservable();
  }

  get $script(): Observable<SimpleScript> {
    return this.scriptSubject.asObservable();
  }

  get $isEditing(): Observable<boolean> {
    return this.isEditingSubject.asObservable();
  }

  get $isUploading(): Observable<boolean> {
    return this.isUploadingSubject.asObservable();
  }

  setScript(script: SimpleScript): void {
    this.script = script;
    this.scriptSubject.next(this.script);
  }

  setIsEditing(isEditing: boolean): void {
    this.isEditing = isEditing;
    this.isEditingSubject.next(this.isEditing);
  }

  setIsUploading(isUploading: boolean): void {
    this.isUploading = isUploading;
    this.isUploadingSubject.next(this.isUploading);
  }

  setSelectedRole(role: Role): void {
    this.selectedRole = this.selectedRole?.name === role.name ? null : role;
    this.selectedRoleSubject.next(this.selectedRole);
  }
}

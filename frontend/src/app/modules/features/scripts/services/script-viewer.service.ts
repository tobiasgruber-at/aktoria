import {Role, SimpleScript} from '../../../shared/dtos/script-dtos';
import {BehaviorSubject, Observable} from 'rxjs';
import {SimpleSection} from '../../../shared/dtos/section-dtos';

/** Local service for viewing or editing scripts. */
export class ScriptViewerService {
  /** Whether the script viewer is editable, or read-only. */
  private isEditingScript = false;
  private isEditingScriptSubject = new BehaviorSubject<boolean>(
    this.isEditingScript
  );
  private isUploading = false;
  private isUploadingSubject = new BehaviorSubject<boolean>(this.isUploading);
  private markedSection: SimpleSection = null;
  private markedSectionSubject = new BehaviorSubject<SimpleSection>(
    this.markedSection
  );
  private script: SimpleScript = null;
  private scriptSubject = new BehaviorSubject<SimpleScript>(this.script);
  private selectedRole: Role = null;
  private selectedRoleSubject = new BehaviorSubject<Role>(this.selectedRole);
  /**
   * Indicates how many loadings are currently stacked.
   *
   * @description It might be that setLoading(true) was called multiple times, before the first loading was completed by
   * setLoading(false). In this case, it would stop loading, although the second loading has not completed yet. That's why
   * the loadings are counted.
   */
  private loadingCounter = 0;
  private loadingSubject = new BehaviorSubject<boolean>(
    this.loadingCounter > 0
  );

  get $script(): Observable<SimpleScript> {
    return this.scriptSubject.asObservable();
  }

  get $selectedRole(): Observable<Role> {
    return this.selectedRoleSubject.asObservable();
  }

  /** @see isEditingScript */
  get $isEditingScript(): Observable<boolean> {
    return this.isEditingScriptSubject.asObservable();
  }

  get $isUploading(): Observable<boolean> {
    return this.isUploadingSubject.asObservable();
  }

  get $markedSection(): Observable<SimpleSection> {
    return this.markedSectionSubject.asObservable();
  }

  get $loading(): Observable<boolean> {
    return this.loadingSubject.asObservable();
  }

  setScript(script: SimpleScript): void {
    this.script = script;
    this.scriptSubject.next(this.script);
  }

  setSelectedRole(role: Role): void {
    this.selectedRole = this.selectedRole?.name === role.name ? null : role;
    this.selectedRoleSubject.next(this.selectedRole);
  }

  setIsEditingScript(isEditingScript: boolean): void {
    this.isEditingScript = isEditingScript;
    this.isEditingScriptSubject.next(this.isEditingScript);
  }

  setIsUploading(isUploading: boolean): void {
    this.isUploading = isUploading;
    this.isUploadingSubject.next(this.isUploading);
  }

  setMarkedSection(markedSection: SimpleSection): void {
    this.markedSection = markedSection;
    this.markedSectionSubject.next(this.markedSection);
  }

  setLoading(loading: boolean): void {
    if (loading) {
      this.loadingCounter++;
    } else {
      this.loadingCounter--;
    }
    this.loadingSubject.next(this.loadingCounter > 0);
  }
}

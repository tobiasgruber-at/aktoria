import {Role, SimpleScript} from '../../../shared/dtos/script-dtos';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import {SimpleSection} from '../../../shared/dtos/section-dtos';

export interface MarkedSection {
  section: SimpleSection;
  scrollTo: boolean;
}

export type IsMarkingSection = 'start' | 'end';

/**
 * Local service for a script viewer.<br>
 * Should be <strong>injected to the parent</strong> of a script viewer, so that the state is encapsulated to this
 * instance of a viewer.<br>
 * Includes states for viewing, editing and selecting sections.
 */
export class ScriptViewerService {
  /** Whether the script viewer is editable, or read-only. */
  private isEditingScriptSubject = new BehaviorSubject<boolean>(null);
  private isUploadingSubject = new BehaviorSubject<boolean>(null);
  private isMarkingSection: IsMarkingSection = null;
  private isMarkingSectionSubject = new BehaviorSubject<IsMarkingSection>(
    this.isMarkingSection
  );
  /** Section that should be marked on the script viewer. */
  private markedSectionSubject = new BehaviorSubject<MarkedSection>(null);
  private scriptSubject = new BehaviorSubject<SimpleScript>(null);
  private selectedRoleSubject = new BehaviorSubject<Role>(null);
  /** Fires when the conflict of a line was resolved. */
  private resolvedConflictSubject = new BehaviorSubject<number>(null);
  private scrollToLineSubject = new Subject<number>();
  /**
   * Indicates how many loadings are currently stacked.
   *
   * @description It might be that setLoading(true) was called multiple times, before the first loading was completed by
   * setLoading(false). In this case, it would stop loading, although the second loading has not completed yet. That's why
   * the loadings are counted.
   */
  private loadingCounter = 0;
  /** Indicates whether there are still open loadings. */
  private loadingSubject = new BehaviorSubject<boolean>(
    this.loadingCounter > 0
  );

  get $script(): Observable<SimpleScript> {
    return this.scriptSubject.asObservable();
  }

  get $selectedRole(): Observable<Role> {
    return this.selectedRoleSubject.asObservable();
  }

  get $scrollToLine(): Observable<number> {
    return this.scrollToLineSubject.asObservable();
  }

  /** @see isEditingScript */
  get $isEditingScript(): Observable<boolean> {
    return this.isEditingScriptSubject.asObservable();
  }

  get $isUploading(): Observable<boolean> {
    return this.isUploadingSubject.asObservable();
  }

  get $isMarkingSection(): Observable<IsMarkingSection> {
    return this.isMarkingSectionSubject.asObservable();
  }

  /** @see markedSection */
  get $markedSection(): Observable<MarkedSection> {
    return this.markedSectionSubject.asObservable();
  }

  /** @see loadingSubject */
  get $loading(): Observable<boolean> {
    return this.loadingSubject.asObservable();
  }

  /** @see resolvedConflictSubject */
  get $resolveConflict(): Observable<number> {
    return this.resolvedConflictSubject.asObservable();
  }

  setScript(script: SimpleScript): void {
    this.scriptSubject.next(script);
  }

  setSelectedRole(role: Role): void {
    this.selectedRoleSubject.next(
      this.selectedRoleSubject.getValue()?.name === role.name ? null : role
    );
  }

  setIsEditingScript(isEditingScript: boolean): void {
    this.isEditingScriptSubject.next(isEditingScript);
  }

  setIsUploading(isUploading: boolean): void {
    this.isUploadingSubject.next(isUploading);
  }

  setIsMarkingSection(isMarkingSection: IsMarkingSection): void {
    this.isMarkingSection = isMarkingSection;
    this.isMarkingSectionSubject.next(this.isMarkingSection);
  }

  setMarkedSection(markedSection: MarkedSection): void {
    this.markedSectionSubject.next(markedSection);
  }

  setLoading(loading: boolean): void {
    if (loading) {
      this.loadingCounter++;
    } else {
      this.loadingCounter--;
    }
    this.loadingSubject.next(this.loadingCounter > 0);
  }

  scrollToLine(index: number): void {
    this.scrollToLineSubject.next(index);
  }

  setResolveConflict(index: number): void {
    this.resolvedConflictSubject.next(index);
  }
}

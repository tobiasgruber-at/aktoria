import { Component, OnInit } from '@angular/core';
import { SimpleScript } from '../../../../../shared/dtos/script-dtos';
import { ScriptViewerService } from '../../../services/script-viewer.service';
import { ScriptService } from '../../../../../core/services/script/script.service';
import { ActivatedRoute } from '@angular/router';
import { SimpleSection } from '../../../../../shared/dtos/section-dtos';

@Component({
  selector: 'app-script-rehearsal-sections',
  templateUrl: './script-rehearsal-sections.component.html',
  styleUrls: ['./script-rehearsal-sections.component.scss'],
  providers: [ScriptViewerService]
})
export class ScriptRehearsalSectionsComponent implements OnInit {
  getLoading = true;
  script: SimpleScript = null;
  sections: SimpleSection[] = [
    {
      name: 'Abschnitt 1',
      startLine: 1,
      endLine: 20
    },
    {
      name: 'Abschnitt 2',
      startLine: 5,
      endLine: 25
    }
  ];

  constructor(
    public scriptViewerService: ScriptViewerService,
    private scriptService: ScriptService,
    private route: ActivatedRoute
  ) {
    // TODO: remove
    //this.scriptViewerService.setMarkedSection(this.sections[0]);
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = +params.get('id');
      this.scriptService.getOne(id).subscribe({
        next: (script) => {
          this.scriptViewerService.setScript(script);
          this.getLoading = false;
        },
        error: () => {
          // TODO: show error
          //this.getError = 'Skript konnte nicht gefunden werden.';
          this.getLoading = false;
        }
      });
    });
  }

  selectSection(section: SimpleSection): void {
    this.scriptViewerService.setMarkedSection(section);
  }
}
